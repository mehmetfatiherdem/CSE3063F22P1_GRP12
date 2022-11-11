package iteration1.src.data_structures;

public class Tuple<K,V> {
    
    private K key;
    private V value;

    public Tuple(K key,V value) {
        this.key = key;
        this.value = value;
    }

    public K GetKey(){
        return key;
    }

    public V GetValue(){
        return value;
    }
}
