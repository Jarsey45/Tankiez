package cloud.jarsey45.tankiez.util;

public class MouseUtils {
//	public static boolean isMouseOver(double mouseX, double mouseY, int x, int y) {
//		return isMouseOver(mouseX, mouseY, x, y);
//	}

	public static boolean isMouseOver(double mouseX, double mouseY, int x, int y, int sizeX, int sizeY) {
		return (mouseX >= x && mouseX <= x + sizeX) && (mouseY >= y && mouseY <= y + sizeY);
	}
}
