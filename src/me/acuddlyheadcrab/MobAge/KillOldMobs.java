package me.acuddlyheadcrab.MobAge;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;

public class KillOldMobs implements Runnable {
    private static boolean taskDebug = true;
    
    @SuppressWarnings("unused")
    private static MobAge plugin;
    public KillOldMobs(MobAge mobAge) {plugin = mobAge;}
    
    
    @Override
    public void run(){
        int killcountall = 0, agelimit = MobAge.config.getInt("AgeLimit");
        
        for(World world : Bukkit.getServer().getWorlds()){
            
            int killcountworld = 0;
            
            if(world.getPlayers().size()>0){
                
                List<LivingEntity> entlist = world.getLivingEntities();
                
                for(LivingEntity ent : entlist){
                    try {
                      
                      boolean 
                          player = (ent instanceof Player),
                          tamed = ent instanceof Tameable ? ((Tameable) ent).isTamed() : false,
                          invulnerable = ent.spigot().isInvulnerable(),
                          saddled = ent instanceof Pig ? ((Pig) ent).hasSaddle() : false,
                          
                          canKill = !player&&!tamed&&!invulnerable&&!saddled
                      ;
                      
                      if(canKill){
                          if(PluginIO.getWhiteListVal(ent, "age")) {
                              if(ent.getTicksLived()>=agelimit){
                                  ent.remove();
                                  killcountall++;
                                  killcountworld++;
                              }    
                          }
                      }                        
                    } catch (UnsupportedOperationException e) {
                    }
                    
                }
                
                if(killcountworld > 0 && taskDebug){
                    PluginIO.sendPluginInfo("Removed "+killcountworld+" entities from the world "+world.getName());
                }   
            }
        }

        if(killcountall > 0 && taskDebug){
            PluginIO.sendPluginInfo("Removed "+killcountall+" entities");
        }
        
    }
    
}
