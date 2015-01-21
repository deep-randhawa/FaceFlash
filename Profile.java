package randhawa.deep.faceflash;

/**
 * Created by K4therin2 on 1/18/2015.
 */

public class Profile {
    private int memoryTier = 0; //a value between 0 and 7, 0 being not remembered well and 7 being remembered very well
    private String name;
    private String picture;
    //gender?

    public Profile(String name, String picture) {
        setName(name);
        setPicture(picture);
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void setPicture(String picture){
        this.picture = picture;
    }

    public String getPicture(){
        return this.picture;
    }

    public int getMemoryTier(){
        return this.memoryTier;
    }

    public void setMemoryTier(boolean correct){

        if ((correct == true) && (memoryTier < 7)) {
            memoryTier += 1;
        } else {
            if (memoryTier != 0)
                memoryTier -= 1;
        }

    }

}
