import java.io.IOException;

public class DemoRAF {
   //---------------------------------------------------------
   //---------------------------------------------------------
   //---------------------------------------------------------
   //---------------------------------------------------------
   //---------------------------------------------------------
   //---------------------------------------------------------
   //---------------------------------------------------------
   public static void main(String[] args) throws IOException, InterruptedException {
   PP_Tools OBJ = new PP_Tools();
   int T = 12;
   int SIZE = 10;
   String CADENA;
      OBJ.CreateDataFile("Datos1.TXT",T,'H',100,999,SIZE);
      OBJ.CreateDataFile("Datos2.TXT",T,'H',100,999,SIZE);

      OBJ.WriteVectorScreen("Datos1.TXT",T);
      OBJ.WriteVectorScreen("Datos2.TXT",T);

      OBJ.WriteMatrixScreen("Datos1.TXT",6,2);
      OBJ.WriteMatrixScreen("Datos1.TXT",2,6);
      OBJ.WriteMatrixScreen("Datos1.TXT",3,4);
      OBJ.WriteMatrixScreen("Datos1.TXT",4,3);

      OBJ.WriteMatrixScreen("Datos2.TXT",6,2);
      OBJ.WriteMatrixScreen("Datos2.TXT",2,6);
      OBJ.WriteMatrixScreen("Datos2.TXT",3,4);
      OBJ.WriteMatrixScreen("Datos2.TXT",4,3);

      CADENA = OBJ.SumaVectores("Datos1.TXT","Datos2.TXT",T,SIZE);
      OBJ.PrintStringToFile("SumaVectores.TXT", CADENA);
      OBJ.WriteVectorScreen("SumaVectores.TXT",T);

      OBJ.SumaMatrices("Datos1.TXT","Datos2.TXT","SumaMatrices.TXT",6,2,SIZE);
      OBJ.WriteMatrixScreen("Datos1.TXT",6,2);
      OBJ.WriteMatrixScreen("Datos2.TXT",6,2);
      OBJ.WriteMatrixScreen("SumaMatrices.TXT",6,2);


   }
   //---------------------------------------------------------
}
