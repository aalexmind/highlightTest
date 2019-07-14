package base;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import cppImp.ColorJNI;

class EngineTest {

	@Test
	void testGetColor() {
		String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz';:/";
		ColorJNI[] colorArr = Engine.getColor(str);
		for (int i = 0; i < str.length(); i++) {
			if (colorArr[i].r != 0 || colorArr[i].g != 0 || colorArr[i].b != 0) {
				fail("wrong color for letter or symbol");
			}
		}
		str = "1234567890";
		colorArr = Engine.getColor(str);
		for (int i = 0; i < str.length(); i++) {
			if (colorArr[i].r != 0 || colorArr[i].g != 0 || colorArr[i].b != 255) {
				fail("wrong color for digit");
			}
		}
		str = "     ";
		colorArr = Engine.getColor(str);
		for (int i = 0; i < str.length(); i++) {
			if (colorArr[i].r != 255 || colorArr[i].g != 255 || colorArr[i].b != 255) {
				fail("wrong color for space");
			}
		}
		str = "1) My Name is Too Good.\\nI wish I could be ONE of THE BEST";
		colorArr = Engine.getColor(str);
		for (int i = 0; i < str.length(); i++) {
			if (colorArr[i].r < 0 || colorArr[i].r > 255 || colorArr[i].g < 0 || colorArr[i].g > 255
					|| colorArr[i].b < 0 || colorArr[i].b > 255) {
				fail("general out of bounds");
			}
		}

	}

}
