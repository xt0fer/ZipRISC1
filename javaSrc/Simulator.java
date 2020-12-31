import Word;

public class Simulator {
    private CPU cpu;

    public Simulator(CPU cpu) {
        cpu = cpu;
    }

    public static void main(String[] args) {
        Simulator s = new Simulator(new CPU(0x0));
        System.err.println("**** start simulation.");
        //if (args.length > 1) {
            try {
                s.load("a.zex"); // s.load(args[1]);
                s.run();
            } catch (Panic e) {
                e.printStackTrace();
                java.lang.System.exit(-1);
            }    
        //}
        java.lang.System.exit(0);
    }

    private void run() {
        Engine engine = new Engine(this.cpu);
        engine.startAt(0x0000);
    }

    private void load(String executable_filename)  {
        // open and load each line.
        java.io.BufferedReader reader;
		try {
			reader = new java.io.BufferedReader(new java.io.FileReader(
                executable_filename));
			String line = reader.readLine();
			while (line != null) {
                System.err.println(line);
                this.loadMemory(line);
                // read next line
				line = reader.readLine();
			}
			reader.close();
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
    }

    private void loadMemory(String line)  {
        // line should be in this format:
        // hexmem_address byte0 byte1 byte2 byte3 // comments
        
        String[] tokens = line.split("\\s+");
        if (tokens.length < 5)
            throw new Panic("corrupted line in file.");
        int aa = Integer.decode(tokens[0]);
        Word w = new Word(Integer.parseInt(tokens[1], 16),
        Integer.parseInt(tokens[2], 16),
        Integer.parseInt(tokens[3], 16),
        Integer.parseInt(tokens[4], 16));

        cpu.store(aa, w);
    }
}