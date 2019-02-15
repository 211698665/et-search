package cn.henu.search.service;

import cn.henu.common.pojo.SearchResult;

public interface SearchService {

	SearchResult search(String keyword,int page ,int rows) throws Exception;
}
