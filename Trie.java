package structures;

import java.util.ArrayList;

/**
 * This class implements a compressed trie. Each node of the tree is a CompressedTrieNode, with fields for
 * indexes, first child and sibling.
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {
	
	/**
	 * Words indexed by this trie.
	 */
	ArrayList<String> words;
	
	/**
	 * Root node of this trie.
	 */
	TrieNode root;
	
	/**
	 * Initializes a compressed trie with words to be indexed, and root node set to
	 * null fields.
	 * 
	 * @param words
	 */
	public Trie() {
		root = new TrieNode(null, null, null);
		words = new ArrayList<String>();
	}
	
	/**
	 * Inserts a word into this trie. Converts to lower case before adding.
	 * The word is first added to the words array list, then inserted into the trie.
	 * 
	 * @param word Word to be inserted.
	 */
	private TrieNode addingRearNode(TrieNode root, String i,int index)
	{
		TrieNode ptr= root.firstChild;
		TrieNode prev = null;
		if (root.firstChild== null)
		{		
			words.add(i);		
			Indexes toInsert= new Indexes(0,(short)(index+1),(short)(i.length()-1));		
			root.firstChild= new TrieNode(toInsert,null,null);		
			return root;
		}		
		while(ptr != null)
		{		
		prev = ptr;
		ptr = ptr.sibling;
		}
		words.add (i);
	    Indexes toInsert= new Indexes(words.indexOf(i),(short)(index+1),(short)(i.length()-1));	
	    prev.sibling= new TrieNode(toInsert,null,null);
	return root;	
	}
	public void insertWord(String word)
	{
        word= word.toLowerCase();
		if (word.length()<2)
		{
			root = addingRearNode(root,word,-1);
			return;
		}
		Node(root,word,-1);
	}
	private  int insertIndex(String compareTo, String input, int index) 
	{		
		int i;
		for ( i = index + 1; i < compareTo.length()  && i < input.length(); i++) 
		{
			if (compareTo.charAt(i) != input.charAt(i)) 
			{
				break;
			}
		}
		if (i== input.length() || i ==compareTo.length())
		{			
			return i-2;
		}
		return i-1;
	}
	
	private void Node(TrieNode root, String input, int x) 
	{
		boolean check =false;
		if (root.firstChild == null) 
		{
			root = root.substr == null || root.firstChild == null ?addingRearNode(root, input, -1)
					: addingRearNode(root, input, root.substr.endIndex);
			return;
		}
		TrieNode ptr = root.firstChild;
		TrieNode internal = null;
		TrieNode internalPrev = null;
		TrieNode prev = root;
		int max = x;
		String temp = "";
		while (ptr != null) 
		{
			temp = words.get(ptr.substr.wordIndex);
			if (ptr.firstChild == null) 
			{
				if (temp.charAt(0) == input.charAt(0))
				{
					int track = insertIndex(temp, input, 0);
					if (track > x) 
					{
						max = track;
						internalPrev = prev;
						internal = ptr;
					}
				}
			}
			else if (ptr.substr.endIndex < input.length() - 1 && ptr.substr.endIndex > max
					&& temp.substring(0, ptr.substr.endIndex + 1).equals(input.substring(0, ptr.substr.endIndex + 1)))
			{
				check = true;				
				internalPrev = prev;				
				internal = ptr;
				max = ptr.substr.endIndex;

			} 
			else  if(ptr.firstChild!=null && temp.charAt(0)== input.charAt(0))
			{				
				int track1= insertIndex(temp,input,0);				
				if (track1>x)
				{					
					internalPrev = prev;					
					internal = ptr;
					max= track1;
				}
			} 
			prev = ptr;
			ptr = ptr.sibling;
		} 
		if (internal == null) 
		{			
			root = root.substr == null || root.firstChild == null ? addingRearNode(root, input, -1)					
					: addingRearNode(root, input, root.substr.endIndex);
			return;
		}
		else if (internal.firstChild == null) 
		{
			temp = words.get(internal.substr.wordIndex);			
			words.add(input);			
			int sameIndex = internal.substr.wordIndex;			
			Indexes toInsert = new Indexes(sameIndex, (short) (max + 1), (short) (temp.length() - 1));			
			int indexOfNewChild = words.indexOf(input);			
			Indexes rightChild = new Indexes(indexOfNewChild, (short) (max + 1), (short) (input.length() - 1));
			internal.firstChild = new TrieNode(toInsert, null, new TrieNode(rightChild, null, null));
			toInsert = new Indexes(sameIndex, (short) (x + 1), (short) (max));			
			if ( internalPrev.firstChild!=null&&internalPrev.firstChild.equals( internal)) 
			{
				internalPrev.firstChild = new TrieNode(toInsert, internal.firstChild, internal.sibling);
			}
			else 
			{
				internalPrev.sibling = new TrieNode(toInsert, internal.firstChild, internal.sibling);
			}			
		}
		else if(internal.firstChild!= null && internal.substr != null && (check!=true))
		{			
			words.add(input);			
			Indexes indexes = new Indexes(words.indexOf(input),(short)(0),(short)(max));			
			internal.substr.startIndex= (short)(max+1);			
			if (internalPrev.firstChild!=null&&internalPrev.firstChild.equals(internal))
			{			
			TrieNode firstChild= new TrieNode (new Indexes(words.indexOf(input),(short)(max+1),(short)(input.length())),null,internalPrev.firstChild);			
			TrieNode toInsert = new TrieNode(indexes,firstChild,internal.sibling);			
			internalPrev.firstChild = toInsert;
			}
			else
			{				
				TrieNode firstChild= new TrieNode (new Indexes(words.indexOf(input),(short)(max+1),(short)(input.length())),null,internalPrev.sibling);				
				TrieNode toInsert = new TrieNode(indexes,firstChild,internal.sibling);				
				internalPrev.sibling= toInsert;				
			}
		}
		else 
		{
			Node(internal, input, max);
		}
		return;
	}
		/**
	 * Given a string prefix, returns its "completion list", i.e. all the words in the trie
	 * that start with this prefix. For instance, if the tree had the words bear, bull, stock, and bell,
	 * the completion list for prefix "b" would be bear, bull, and bell; for prefix "be" would be
	 * bear and bell; and for prefix "bell" would be bell. (The last example shows that a prefix can be
	 * an entire word.) The order of returned words DOES NOT MATTER. So, if the list contains bear and
	 * bell, the returned list can be either [bear,bell] or [bell,bear]
	 * 
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all words in tree that start with the prefix, order of words in list does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public ArrayList<String> completionList(String prefix) 
	{
		TrieNode ptr = this.root.firstChild;		
		ArrayList<String> result = new ArrayList<>();		
		if (root.firstChild==null)
		{			
			return result;
		}		
		while(ptr !=null){		
			String value = words.get(ptr.substr.wordIndex);			
			if (value.charAt(0)== prefix.charAt(0))
			{				
				if (value.contains(prefix))
				{
					/*	
					String value = words.get(Node.substr.wordIndex);			
					if (value.charAt(0)== prefix.charAt(0))
					{				
						if (value.contains(prefix))
						{
							if (Node.firstChild==null && value.equals(prefix))
							{
								result.add(value);
								return result;
							}
							else
							{
								result = prefix(Node.firstChild,result,prefix);						
								return result;
							}
						}
						result = prefix(Node.firstChild,result,prefix);
					}
					Node = Node.sibling;
				}
				return result;*/
					if (ptr.firstChild==null
							&&( prefix.length()<=value.length()
							&&prefix.equals(value.substring(0,prefix.length()))))
					{
						result.add(value);
						return result;
					}
					else
					{
						result = prefix(ptr.firstChild,result,prefix);					
						return result;
					}
				}
				result = prefix(ptr.firstChild,result,prefix);
			}
			ptr = ptr.sibling;
		}
	 return result;
	}
	
	private ArrayList<String> prefix(TrieNode input, ArrayList<String> listOfArray,String prfx)
	{	
		String s = words.get(input.substr.wordIndex);
		/*String s = words.get(input.substr.wordIndex);		
		if (input.firstChild==null
				&& s.contains(prfx))
		{			
			listOfArray.add(words.get(input.substr.wordIndex));			
			if (input.sibling!=null)
			{				
				listOfArray=prefix(input.sibling,listOfArray,prfx);
			}
			return listOfArray;			
		} else if(input.firstChild!=null)
		{			
			listOfArray = prefix(input.firstChild,listOfArray,prfx);
		}
		if  (input.sibling!=null)
		{			
			listOfArray = prefix(input.sibling,listOfArray,prfx);
		}
		return listOfArray;*/
		if (input.firstChild==null
				&& ( prfx.length()<=s.length()
				&&prfx.equals(s.substring(0,prfx.length()))))
		{			
			listOfArray.add(words.get(input.substr.wordIndex));			
			if (input.sibling!=null)
			{			
				listOfArray=prefix(input.sibling,listOfArray,prfx);
			}
			return listOfArray;			
		} 
		else if(input.firstChild!=null)
		{			
			listOfArray = prefix(input.firstChild,listOfArray,prfx);
		}
		if  (input.sibling!=null)
		{			
			listOfArray = prefix(input.sibling,listOfArray,prfx);
		}
		return listOfArray;
	}
	
	
	public void print() {
		print(root, 1, words);
	}
	
	private static void print(TrieNode root, int indent, ArrayList<String> words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			System.out.println("      " + words.get(root.substr.wordIndex));
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		System.out.println("(" + root.substr + ")");
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }
