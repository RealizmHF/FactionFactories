package io.github.RealizmHF;

import java.io.File;
import java.io.IOException;

import org.bukkit.World;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.BlockWorldVector;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.EditSessionFactory;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.BlockID;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.io.SchematicReader;
import com.sk89q.worldedit.foundation.Block;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import com.sk89q.worldedit.schematic.MCEditSchematicFormat;
import com.sk89q.worldedit.world.DataException;

public class RegionPaster {
	  
    private EditSessionFactory editSessionFactory;
    private World w;
    private File f;
    private Vector or;
    private EditSession editSession;

    public RegionPaster(EditSessionFactory editSessionFactory) {
        this.editSessionFactory = editSessionFactory;
    }


    @SuppressWarnings("deprecation")
	public void loadArea(World world, File file, Vector origin) throws DataException, IOException, MaxChangedBlocksException {
    	
    	this.w = world;
    	this.f = file;
    	this.or = origin;
    	
    	
        editSession = editSessionFactory.getEditSession((com.sk89q.worldedit.world.World) new BukkitWorld(world), 999999999);
        MCEditSchematicFormat.getFormat(file).load(file).paste(editSession, origin, false);
    }
}