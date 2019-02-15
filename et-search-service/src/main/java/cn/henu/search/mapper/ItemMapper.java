package cn.henu.search.mapper;

import java.util.List;

import cn.henu.common.pojo.SearchItem;

public interface ItemMapper {

	List<SearchItem> getItemList();
	SearchItem getItemById(long itemId);
}
