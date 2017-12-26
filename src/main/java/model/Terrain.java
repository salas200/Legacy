package model;


import service.ResourceService;

public class Terrain extends Sprite {

    /**
     * Allocates a new image object using the given path.
     *
     * @param path Image preferably a PNG format
     */
    public Terrain(String path) {
        super(ResourceService.loadImage(path));
        this.setImage(ResourceService.loadImage(path));
    }
}
