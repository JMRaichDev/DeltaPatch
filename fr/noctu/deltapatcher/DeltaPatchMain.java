package fr.noctu.deltapatcher;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.lang.reflect.Field;
import java.util.Vector;

@Mod(modid = DeltaPatchMain.MODID, version = DeltaPatchMain.VERSION)
public class DeltaPatchMain {
    public static final String MODID = "deltapatch";
    public static final String VERSION = "1.0";

    @EventHandler
    @SideOnly(Side.CLIENT)
    public void postInit(FMLPostInitializationEvent event) throws NoSuchFieldException, IllegalAccessException {
        Field f = ClassLoader.class.getDeclaredField("classes");
        f.setAccessible(true);

        ((Vector<Class<?>>) f.get(ClassLoader.getSystemClassLoader())).forEach((c) -> {
            if(c.getName().contains("delta")) {
                System.out.println("delta detected : " + c.getName() + " classloader: System");
                FMLCommonHandler.instance().exitJava(-1, true);
            }
        });

        ((Vector<Class<?>>) f.get(Thread.currentThread().getContextClassLoader())).forEach((c) -> {
            if(c.getName().contains("delta")) {
                if(!c.getName().contains("cpw.mods.fml.repackage.com.nothome.delta.") && !c.getName().contains("fr.noctu.deltapatcher.")){
                    System.out.println("delta detected : " + c.getName() + " classloader: Current Thread");
                    FMLCommonHandler.instance().exitJava(-1, true);
                }
            }
        });

        ((Vector<Class<?>>) f.get(Loader.instance().getModClassLoader())).forEach((c) -> {
            if(c.getName().contains("delta")) {
                if(!c.getName().contains("cpw.mods.fml.repackage.com.nothome.delta.") && !c.getName().contains("fr.noctu.deltapatcher.")) {
                    System.out.println("delta detected : " + c.getName() + " classloader: Mods");
                    FMLCommonHandler.instance().exitJava(-1, true);
                }
            }
        });
    }
}
