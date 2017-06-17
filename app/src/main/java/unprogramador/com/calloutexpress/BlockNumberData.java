package unprogramador.com.calloutexpress;

/**
 * Created by CesarFlores on 16-Jun-17.
 */

public class BlockNumberData {
    protected long id;
    protected String Name;
    protected String Phone;
    public BlockNumberData (long _id, String _Name, String _Phone){
        this.id = _id;
        this.Name = _Name;
        this.Phone = _Phone;
    }
    public long getId(){return id;}
    public  String getName(){return Name;}
    public  String getPhone(){return Phone;}

    public void setId(long id){this.id = id;}
    public void setName(String Name){this.Name = Name;}
    public void setPhone(String Phone){this.Phone = Phone;}

}
