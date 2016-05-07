package com.capiq.sec.filling.analysis.parse.jflex;

import java.util.HashMap;
import java.util.LinkedList;

public class JflexTokenDataStructure {
	
	private LinkedList<JflexToken> tokenRawList = new LinkedList<JflexToken>();
	private HashMap<JflexTokenType, LinkedList<Integer>> indexMap = new HashMap<JflexTokenType, LinkedList<Integer>>();
	
	public void addToken(JflexToken token){
		tokenRawList.add(token);
		
		JflexTokenType type = token.getTokenType();
		LinkedList<Integer> list = null;
		
		if(indexMap.containsKey(type)){
			list = indexMap.get(type);
			list.add(tokenRawList.size() -1 );
		}else{
			list = new LinkedList<Integer>();
			list.add(tokenRawList.size() -1);
			indexMap.put(type, list);
		}
	}
	
	public JflexToken getToken(Integer index){
		return tokenRawList.get(index);
	}
	
	public LinkedList<JflexToken> getAllToken(){
		return tokenRawList;
	}
	
	public LinkedList<Integer> getTokenTypeIndexList(JflexTokenType type){
		return indexMap.get(type);
	}
	
	public boolean containTokenType(JflexTokenType type){
		return indexMap.containsKey(type);
	}
	
	public HashMap<JflexTokenType, LinkedList<Integer>> getAllTokenType(){
		return indexMap;
	}
	
	public void putTokenTypeIndex(JflexTokenType type, LinkedList<Integer> list){
		indexMap.put(type, list);
	}
	
	public void removeTokenTypeIndex(JflexTokenType type, Integer index){
		if(indexMap.containsKey(type)){
			LinkedList<Integer> indexList = indexMap.get(type);
			indexList.remove(index);
		}
	}
}
