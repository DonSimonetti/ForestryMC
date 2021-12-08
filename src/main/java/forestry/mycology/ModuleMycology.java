package forestry.mycology;

import forestry.api.modules.ForestryModule;
import forestry.core.config.Constants;
import forestry.modules.BlankForestryModule;
import forestry.modules.ForestryModuleUids;

@ForestryModule(containerID = Constants.MOD_ID, moduleID = ForestryModuleUids.MYCOLOGY, name = "Mycology", author = "MettSmirnov", url = Constants.URL, unlocalizedDescription = "for.module.mycology.description")
public class ModuleMycology extends BlankForestryModule
{
    public ModuleMycology()
    {

    }
}
