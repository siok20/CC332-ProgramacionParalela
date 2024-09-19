
package uni.cc332.productovectorial;

import java.io.FileWriter;
import java.io.IOException;

public class Tools {
    public static void PrintMatrixToScreen(int MTX[][], String TITLE) {
int N = MTX.length;
	System.out.println(TITLE);
	for(int i=0;i<=N-1;i++) {
		for(int j=0;j<=N-1;j++) {
			System.out.print(MTX[i][j] + "\t");
		}
		System.out.println();
	}
	System.out.println();
}
//--------------------------------------------
public void PrintMatrixToFile(int MTX[][], String TITLE, String FILENAME) {
int N = MTX.length;
	try {
	    FileWriter FW = new FileWriter(FILENAME);
		FW.write(TITLE + "\n");
		for(int i=0;i<=N-1;i++) {
			for(int j=0;j<=N-1;j++) {
				FW.write(MTX[i][j] + "\t");
			}
			FW.write("\n");
		}
		FW.write("\n");
	    FW.close();
	}
	catch (IOException E) {
	    System.out.print(E.getMessage());
	}
}
//--------------------------------------------
public void PrintMatrixToHTML(int MTX[][], String TITLE, String FILENAME) {
int N = MTX.length;
	try {
	    FileWriter FW = new FileWriter(FILENAME);
		FW.write("<H1>" + TITLE + "</H1>");
		FW.write("<HTML>");
		FW.write("<HEAD>");
		FW.write("</HEAD>");
		FW.write("<BODY>");
		FW.write("<TABLE BORDER=1 CELLPADDING=7  CELLSPACING=0  BGCOLOR=#AED6F1>"); //#A3E4D7
		for(int i=0;i<=N-1;i++) {
		    FW.write("<TR>");
			for(int j=0;j<=N-1;j++) {
				if(i==j) {
				   FW.write("<TD BGCOLOR=#FADBD8>");
				}
				else {
				   FW.write("<TD>");
				}
			    FW.write(MTX[i][j] + "</TD>");
			}
		    FW.write("</TR>");
		}

		FW.write("</TABLE>");
		FW.write("</BODY>");
		FW.write("</HTML>");

	    FW.close();
	}
	catch (IOException E) {
	    System.out.print(E.getMessage());
	}
}
//--------------------------------------------
//--------------------------------------------
}
