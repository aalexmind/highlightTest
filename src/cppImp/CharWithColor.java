package cppImp;

public class CharWithColor {
	private Character ch;
	private ColorJNI color;

	public CharWithColor() {
		this.ch = Character.MIN_VALUE;
		this.color = new ColorJNI();
	}

	public CharWithColor(Character ch, ColorJNI color) {
		this.ch = ch;
		this.color = color;
	}

	public Character getCh() {
		return ch;
	}

	public void setCh(Character ch) {
		this.ch = ch;
	}

	public ColorJNI getColor() {
		return color;
	}

	public void setColor(ColorJNI color) {
		this.color = color;
	}

}
