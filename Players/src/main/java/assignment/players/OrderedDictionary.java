package assignment.players;

public class OrderedDictionary implements OrderedDictionaryADT {

    Node root;

    OrderedDictionary() {
        root = new Node();
    }

    /**
     * Returns the Record object with key k, or it returns null if such a record
     * is not in the dictionary.
     *
     * @param k
     * @return
     * @throws assignment/players/DictionaryException.java
     */
    @Override
    public PlayerRecord find(DataKey k) throws DictionaryException {
        Node current = root;
        int comparison;
        if (root.isEmpty()) {         
            throw new DictionaryException("There is no record matches the given key");
        }

        while (true) {
            comparison = current.getData().getDataKey().compareTo(k);
            if (comparison == 0) { // key found
                return current.getData();
            }
            if (comparison == 1) {
                if (current.getLeftChild() == null) {
                    // Key not found
                    throw new DictionaryException("There is no record matches the given key");
                }
                current = current.getLeftChild();
            } else if (comparison == -1) {
                if (current.getRightChild() == null) {
                    // Key not found
                    throw new DictionaryException("There is no record matches the given key");
                }
                current = current.getRightChild();
            }
        }

    }


    /**
     * Inserts r into the ordered dictionary. It throws a DictionaryException if
     * a record with the same key as r is already in the dictionary.
     *
     * @param /r
     * @throws //players.DictionaryException
     */
    @Override
    public void insert(PlayerRecord r) throws DictionaryException {

        //new player inserted into the dictionary recursively
        root = insertRecursively(root,r);

    }

    /**
     * This method is a helper that inserts a node into the dictionary recursively
     * if there is a key with the same record an expection is thrown that it is already in the dictionary
     * @param curr
     * @param r
     * @return
     * @throws DictionaryException
     */

    private Node insertRecursively(Node curr, PlayerRecord r) throws DictionaryException{

        //Base case
        if(curr == null){
            return new Node(r);
        }

        int comparison = curr.getData().getDataKey().compareTo(r.getDataKey());

        if (comparison > 0){
            curr.setLeftChild(insertRecursively(curr.getLeftChild(),r));
        }else if(comparison < 0) {
            curr.setRightChild(insertRecursively(curr.getRightChild(),r));
        }else{
            throw new DictionaryException("This key is already in the dictionary");
        }
        return curr;
    }

    /**
     * Removes the record with Key k from the dictionary. It throws a
     * DictionaryException if the record is not in the dictionary.
     *
     * @param k
     * @throws players.DictionaryException
     */
    @Override
    public void remove(DataKey k) throws DictionaryException {
        root = removeRecursively(root,k);
    }

    /**
     * This node recursively removes the record k from the dictionary. It throws a
     * DictionaryExpeption if the record is not in the dictionary
     * @param curr
     * @param k
     * @return
     * @throws DictionaryException
     */
    private Node removeRecursively(Node curr, DataKey k)throws DictionaryException {

        if (curr == null) {
            throw new DictionaryException("The key to be reomved could not be found");
        }

        int comparison = curr.getData().getDataKey().compareTo(k);

        if (comparison > 0) {
            curr.setLeftChild(removeRecursively(curr.getLeftChild(), k));
        } else if (comparison < 0) {
            curr.setRightChild(removeRecursively(curr.getRightChild(), k));
        } else {

            if (curr.isLeaf()) {
                return null;
            } else if (curr.getLeftChild() == null) {
                return curr.getRightChild();
            } else if (curr.getRightChild() == null) {
                return curr.getLeftChild();
            } else {

                Node predecessor = findRightNode(curr.getLeftChild());
                curr.setData(predecessor.getData());
                curr.setLeftChild(removeRecursively(curr.getLeftChild(), predecessor.getData().getDataKey()));

            }


        }
        return curr;
    }

    /**
     * This returns the right most Node in the tree
     * @param curr
     * @return
     */
    private Node findRightNode(Node curr){

        while(curr.getRightChild() != null){
            curr = curr.getRightChild();
        }
        return curr;
    }


    /**
     * Returns the successor of k (the record from the ordered dictionary with
     * the smallest key larger than k); it returns null if the given key has no
     * successor. The given key DOES NOT need to be in the dictionary.
     *
     * @param k
     * @return
     * @throws players.DictionaryException
     */
    @Override
    public PlayerRecord successor(DataKey k) throws DictionaryException{
        Node targetNode = findNode(root,k);

        if(targetNode == null){
            throw new DictionaryException("Key not in dictionary");
        }

        if(targetNode.getRightChild() != null){

            Node current = targetNode.getRightChild();

            while(current.getLeftChild() != null){
                current = current.getLeftChild();
            }
            return current.getData();
        }

        //if there is no right child

        Node ances = null;
        Node curr = root;

        while(curr!= null){

            int comaparison = curr.getData().getDataKey().compareTo(k);

            if(comaparison > 0){
                ances = curr;
                curr = curr.getLeftChild();
            } else if (comaparison < 0) {
                curr = curr.getRightChild();
            }else{
                break;
            }

        }

        if(ances != null){
            return ances.getData();
        }else{
            throw new DictionaryException("This key does not have a successor");
        }

    }

    /**
     * This searches the tree for the K record in the tree and returns it.
     * It is a helper for the successor and predecessor methods
     * @param curr
     * @param k
     * @return
     */
    private Node findNode(Node curr, DataKey k){

        if(curr == null){
            return null;
        }

        int comparison = curr.getData().getDataKey().compareTo(k);
        if(comparison == 0){
            return curr;
        }else if(comparison > 0){
            return findNode(curr.getLeftChild(),k);
        }else {
            return findNode(curr.getRightChild(),k);
        }

    }

    /**
     * Returns the predecessor of k (the record from the ordered dictionary with
     * largest key smaller than k; it returns null if the given key has no
     * predecessor. The given key DOES NOT need to be in the dictionary.
     *
     * @param k
     * @return
     * @throws players.DictionaryException
     */
    @Override
    public PlayerRecord predecessor(DataKey k) throws DictionaryException{

        Node targetNode = findNode(root,k);

        if(targetNode == null){
            throw new DictionaryException("Key not in dictionary");
        }

        if(targetNode.getLeftChild() != null){
            Node current = targetNode.getLeftChild();
            while(current.getRightChild() != null){
                current = current.getRightChild();
            }
            return current.getData();
        }

        //If there is no left node

        Node ances = null;
        Node current = root;

        while(current != null){

            int comparison = current.getData().getDataKey().compareTo(k);

            if(comparison < 0){
                ances = current;
                current = current.getRightChild();
            }else if(comparison > 0){
                current = current.getLeftChild();
            }else{
                break;
            }

        }

        if(ances != null){
            return ances.getData();
        }else{
            throw new DictionaryException("There is no predecessor for this key");
        }

    }

    /**
     * Returns the record with smallest key in the ordered dictionary. Returns
     * null if the dictionary is empty.
     *
     * @return
     */
    @Override
    public PlayerRecord smallest() throws DictionaryException{

        //if the root is null then throw the exception
        if (root == null) {
            throw new DictionaryException("Dictionary is empty");
        }

        Node current = root;

        while(current.getLeftChild() != null){
            current = current.getLeftChild();
        }

        return current.getData();

        // change this statement
    }

    /*
     * Returns the record with largest key in the ordered dictionary. Returns
     * null if the dictionary is empty.
     */
    @Override
    public PlayerRecord largest() throws DictionaryException{

        //if the root is null then throw the exception
        if (root == null) {
            throw new DictionaryException("Dictionary is empty");
        }

        Node current = root;

        while(current.getRightChild() != null){
            current = current.getRightChild();
        }

        return current.getData();

    }

    /* Returns true if the dictionary is empty, and true otherwise. */
    @Override
    public boolean isEmpty (){
        return root.isEmpty();
    }
}