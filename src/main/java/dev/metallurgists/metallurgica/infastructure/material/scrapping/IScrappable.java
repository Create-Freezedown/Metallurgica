package dev.metallurgists.metallurgica.infastructure.material.scrapping;

import dev.metallurgists.metallurgica.infastructure.material.Material;

public interface IScrappable {

    ScrappingData getScrappingData(Material mainMaterial);
}
