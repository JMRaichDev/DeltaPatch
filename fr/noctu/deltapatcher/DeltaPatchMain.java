package fr.noctu.deltapatcher;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.relauncher.Side;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Vector;

@Mod(modid = DeltaPatchMain.MODID, version = DeltaPatchMain.VERSION)
public class DeltaPatchMain {
    public static final String MODID = "deltapatch";
    public static final String VERSION = "1.0";

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) throws NoSuchFieldException, IllegalAccessException {
        if(FMLCommonHandler.instance().getSide() == Side.CLIENT){
            Field f = ClassLoader.class.getDeclaredField("classes");
            f.setAccessible(true);

            ClassLoader systemClassLoaderlassLoader = ClassLoader.getSystemClassLoader();
            ClassLoader curThreadLoaderlassLoader = Thread.currentThread().getContextClassLoader();
            ClassLoader forgeClassLoader = Loader.instance().getModClassLoader();

            Vector<Class> systemClasses =  (Vector<Class>) f.get(systemClassLoaderlassLoader);
            Vector<Class> curThreadClasses =  (Vector<Class>) f.get(curThreadLoaderlassLoader);
            Vector<Class> modsClasses =  (Vector<Class>) f.get(forgeClassLoader);

            for(Class c : systemClasses) {
                if(c.getName().contains("delta")) {
                    System.out.println("delta detect : " + c.getName() + " classloader: System");
                    FMLCommonHandler.instance().exitJava(-1, true);
                }
            }
            for(Class c : curThreadClasses){
                if(c.getName().contains("delta")) {
                    if(!c.getName().contains("cpw.mods.fml.repackage.com.nothome.delta.") && !c.getName().contains("fr.noctu.deltapatcher.")){
                        System.out.println("delta detect : " + c.getName() + " classloader: Current Thread");
                        FMLCommonHandler.instance().exitJava(-1, true);
                    }
                }
            }
            for(Class c : modsClasses){
                if(c.getName().contains("delta")) {
                    if(c.getName().contains("delta")) {
                        if(!c.getName().contains("cpw.mods.fml.repackage.com.nothome.delta.") && !c.getName().contains("fr.noctu.deltapatcher.")){
                            System.out.println("delta detect : " + c.getName() + " classloader: Mods");
                            FMLCommonHandler.instance().exitJava(-1, true);
                        }
                    }
                }
            }
        }
    }
}
