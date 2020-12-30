

public class Word {
    int[] b = new int[4];

	public Word(int i) {
		this.b[0] = i & 0x000000FF;
		this.b[1] = i & 0x0000FF00;
		this.b[2] = i & 0x00FF0000;
		this.b[3] = i & 0xFF000000;
	}

	public int getInt() {
		return ((b[3] & 0xFF) << 24) | 
		((b[2] & 0xFF) << 16) | 
		((b[1] & 0xFF) << 8 ) | 
		((b[0] & 0xFF) << 0 );
	}
}