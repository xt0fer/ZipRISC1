import java.io.IOException;

public class Engine {

    private CPU cpu = null;

	public Engine(CPU cpu) {
        this.cpu = cpu;
    }
    
    // opcodes
    public final static int HLT = 0x00;
    public final static int BRZ = 0x6;
    public final static int LD = 0x8;
    public final static int ADD = 0x1;
    public final static int DUMP = 0x0F;
    

    public void startAt(int initial_address) throws PanicException {
        if (initial_address >= CPU.MEMORY_LIMIT) {
            throw new PanicException("memory violation");
        }

        cpu.set(CPU.PC, initial_address);
        
        cpu.statusWord = 0; // running
        while (cpu.statusWord == 0) {
            int aa = cpu.get(CPU.PC);
            Word tw = cpu.fetch(aa);
            cpu.wset(CPU.IR, tw);
            cpu.set(CPU.PC, cpu.get(CPU.PC)+1);

            this.decodeAndExecute(cpu.opcode(),
                cpu.arg1(), cpu.arg2(), cpu.arg3());
    
        }
        // on Exit
        this.cpu.dumpState();
	}

    private void decodeAndExecute(int opcode, int arg1, int arg2, int arg3) {
        switch (opcode) {
            case Engine.HLT:
                cpu.statusWord = -1;
                break;
            case Engine.DUMP:
                cpu.dumpState();
                break;
            case Engine.BRZ:
                branchOnZero(arg1, arg2, arg3);
                break;
            case Engine.ADD:
                add(arg1, arg2, arg3);
                break;
            case Engine.LD:
                loadFromMemory(arg1, arg2, arg3);
                break;
            default:
                // perform a NOP
                ;
        }
    }
    

    private void loadFromMemory(int arg1, int arg2, int arg3) {
        int address = makeAddress(arg2, arg3);
        Word tw = new Word(0);
        try {
            tw = cpu.fetch(address);
        } catch (PanicException e) {
            e.printStackTrace();
        }
        cpu.wset(arg1, tw);

    }

    private void add(int arg1, int arg2, int arg3) {
        int t = cpu.get(arg2) + cpu.get(arg3);
        cpu.set(arg1, t);
    }

    private void branchOnZero(int arg1, int arg2, int arg3) {
        int address = makeAddress(arg2, arg3);
        if (cpu.get(arg1)==0) {
            cpu.set(CPU.PC, address);
        }
    }

    private int makeAddress(int arg2, int arg3) {
        int addr = ((arg2 & 0x0000FF00) | (arg3 & 0x000000FF));
        if (addr < 0 || addr >= CPU.MEMORY_LIMIT) {
            System.out.printf("Engine: Bad Address: %X \n", addr);
            throw new Error("Engine: makeAddress: made errorious address.");
        }
        return addr;
    }

/* the engine should be the "microcode" runner, the object that does all the actual work of the processor */


}
