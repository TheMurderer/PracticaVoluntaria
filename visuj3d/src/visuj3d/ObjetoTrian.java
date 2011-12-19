/*
 * ObjetoTrian.java
 *
 * Created on 29 de noviembre de 2006, 17:17
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package visuj3d;

/**
 *
 * @author lidia
 */

import javax.media.j3d.TriangleArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.PointArray;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import com.sun.j3d.loaders.objectfile.*;
import com.sun.j3d.loaders.*;
import java.util.*;


class ErrorObjInvalido extends Exception {}
class ErrorRangoObjInvalido extends Exception{}
class ErrorObjNoCerrado extends Exception{}
class ErrorObjCerrado extends Exception{}


public class ObjetoTrian {
    
    protected TriangleArray ta; /*Array de triangulo j3d */
    protected int nPuntos; /** numero de puntos del objeto*/
    protected int formato; /** formato por defecto */
    protected Color3f color; /**color de los triangulos */
    private int tamalog; /** objeto válido solo cuando tamalog = nPuntos */
     
    
    /**Constructor por defecto para un tamaño tama en color amarillo; utilizar addPunto para completarlo*/
    public ObjetoTrian(int tama) {
        nPuntos = tama;
        formato = TriangleArray.COORDINATES|TriangleArray.COLOR_3;
        ta = new TriangleArray (tama,formato);
        tamalog = 0;
        color = new Color3f (1f, 1f, 0f);
    }
    
    /**Constructor por defecto en color c; utilizar addPunto para completarlo*/
    public ObjetoTrian(int tama, Color3f c) {
        nPuntos = tama;
        formato = TriangleArray.COORDINATES|TriangleArray.COLOR_3;
        ta = new TriangleArray (tama,formato);
        tamalog = 0;
        color = new Color3f (c);
    }
    
    /** Aniade una una nueva coordenada al final; el objeto no es valido hasta que este valor no llega a nPuntos */
    public void addPunto (Punto3d p) throws ErrorObjCerrado {
      try { 
        if (nPuntos == tamalog) throw new ErrorObjCerrado();  
        Point3d c = new Point3d (p.getX(), p.getY(), p.getZ());
        ta.setCoordinate(tamalog, c);
        ta.setColor(tamalog, color);
        tamalog+=1;
      }  catch (Exception e){
          
      }  
    }  
       
    /**Construye un objeto triangulado a partir de un vector de Puntos3d*/
    public ObjetoTrian (Punto3d[] np){
        nPuntos = tamalog = np.length;
        formato = TriangleArray.COORDINATES | TriangleArray.COLOR_3; 
        color = new Color3f(1f, 1f, 0f);
        for (int i = 0; i<nPuntos; i++){
            ta.setCoordinate(i, new Point3d (np[i].getX(),np[i].getY(),np[i].getZ()));
            ta.setColor(i,color);
        }
    }
    
    /** Constructor a partir de un objeto obj*/
    public ObjetoTrian (String camino){
	   int flags = ObjectFile.RESIZE;
           ObjectFile f = new ObjectFile(flags);
           Scene s = null;
           try {
              s = f.load(camino);
           }
           catch (Exception e) {
              System.err.println(e);
              System.exit(1);
           }
	   Hashtable namedObjects = s.getNamedObjects();
	   Enumeration e =  namedObjects.keys();
		
           while (e.hasMoreElements()) {
  	    String name = (String) e.nextElement();
	    
	    Shape3D shape = (Shape3D) namedObjects.get(name);
	     
	    for (int i = 0;i < shape.numGeometries() ;i++) {
               // Conseguir los vetices de la geometria de un obj
	       	       
	       GeometryArray geom = (GeometryArray) shape.getGeometry(i);
               
               //! tambien se puede hacer esto para obtener los triangulos
	       //TriangleArray ta = (TriangleArray) shape.getGeometry(i);
	       
	       //puntos tiene toda la geometr�; las coordenadas de cada v�tice est� en las posiciones [3,4,5]
	       float puntos[];
	       puntos = geom.getInterleavedVertices();

               //puntos = ta.getInterleavedVertices();
               
	       int tama_puntos = puntos.length;
	       nPuntos = (int)(tama_puntos / 6);
               color = new Color3f (1f, 0f, 1f);
               formato = TriangleArray.COORDINATES|TriangleArray.COLOR_3;
	       Point3d p1,p2,p3;
	       ta = new TriangleArray(nPuntos,formato);
	       
               int k=1; // k=3 es un triangulo
	       for (int j=0; j<nPuntos; j++){
		   Point3d p = new Point3d(puntos[j*6+3],puntos[j*6+4],puntos[j*6+5]);
                   //System.out.print (" x= "); System.out.print(puntos[j*6+3]); System.out.print(" "); 
		   //System.out.print (" y= "); System.out.print(puntos[j*6+4]); System.out.print(" ");
		   //System.out.print (" z= "); System.out.println(puntos[j*6+5]); 
		   ta.setCoordinate(j,p);
		   ta.setColor(j,new Color3f(color));
		   /*
                   // formar aqu�un triangulo;ejemplo:
		   if (k==1)  p1 = new Point3d (p); 
		   if (k==2)  p2 = new Point3d (p);
		   // con k==3 tengo un nuevo tri�gulo
		   if (k==3) {p3 = new Point3d(p); 
		              // ya tenemos un tri�gulo al que aplicar la intersecci�
			      //Triangulo3d t = new Triangulo3d(p1,p2,p3); 			   
			      k=1;
			      
			      }		  */
	       }
               
	       
	    }	
	}
    }
    
    /**Constructor copia*/
    public ObjetoTrian (ObjetoTrian np) {
        nPuntos = np.nPuntos;
        formato = np.formato;
        color = new Color3f (np.color);
        ta = np.ta;
        tamalog = np.tamalog;
    }
    
    /**Dice el numero de puntos*/
    public int numPuntos(){
        return nPuntos;
    }
    
    boolean nubeValida (){
        return nPuntos == tamalog;
    }
 
    
    /**Dice el color de los puntos*/
    public Color3f getColor(){
        return color;
    }
    
    /**Obtiene el punto del objeto de la posicion indice*/
    public Punto3d getPunto (int indice) throws ErrorRangoObjInvalido{
        Punto3d p = null;
        try{
            Point3d c = new Point3d();
            ta.getCoordinate(indice,c);
            p =  new Punto3d (c.x,c.y,c.z);      
        } catch (Exception e) {
            throw new ErrorRangoObjInvalido(); 
        }
        return p;
    }
    
        
    /** Cambia una una nueva coordenada en la posicion i; la nube no es valida hasta que este valor no llega a nPuntos */
    public void setPunto (Punto3d p, int pos) throws ErrorRangoObjInvalido, ErrorObjNoCerrado{
      try{
        if (pos < 0 || pos > nPuntos) throw new ErrorRangoObjInvalido();
        if (tamalog != nPuntos) throw new ErrorObjNoCerrado();
        Point3d c = new Point3d (p.getX(), p.getY(), p.getZ());
        ta.setCoordinate(pos, c);
        ta.setColor(pos, color);
      } catch (Exception e){
          
      }  
    }  

    /** Obtiene la nube de puntos correspondiente a los vertices del objeto*/
    public NubePuntos3d getNubePuntos () throws ErrorNubeCerrada {
        NubePuntos3d np = new NubePuntos3d(nPuntos);
        Point3d punto = new Point3d();
	for (int j=0; j<nPuntos; j++){
             ta.getCoordinate(j,punto);
             ta.getColor(j,color);
             Punto3d punto3d = new Punto3d (punto);
             punto3d.modifica (punto3d.getX()*Geometria.RANGO, punto3d.getY()*Geometria.RANGO, punto3d.getZ()*Geometria.RANGO);
             //punto3d.out();
             np.addPunto(punto3d);
	 }
         return np;
    }
    
    
    /**Visualiza el poligono en pantalla*/
    public void out(){
        System.out.println("ObjetoTrian:"+ta);
    }

}
