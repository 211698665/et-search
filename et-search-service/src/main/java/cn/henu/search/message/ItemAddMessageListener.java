package cn.henu.search.message;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import cn.henu.common.pojo.SearchItem;
import cn.henu.search.mapper.ItemMapper;

/**
 * 监听商品添加消息，接收消息后，将对应的商品信息同步到索引库
 * @author syw
 *
 */
public class ItemAddMessageListener implements MessageListener {

	@Autowired
	private ItemMapper itemMapper;
	
	@Autowired
	private HttpSolrClient httpSolrClient;
	@Override
	public void onMessage(Message message) {
		//从消息中取商品id
		TextMessage textMessage=(TextMessage)message;
		try {
			String text = textMessage.getText();
			Long itemId=new Long(text);
			//等待et-manager-service的发送消息的提交事务，因为是先查询的ID，可能会出现那边事务还没有提交还没有添加成功，这边就开始查询了
			Thread.sleep(1000);//等待事务提交，还以另一种方法就是在controller层再发消息，这样事务肯定提交了
			//根据商品ID查询商品信息
			SearchItem searchItem = itemMapper.getItemById(itemId);
			//创建一个文档对象
			SolrInputDocument document = new SolrInputDocument();
			//向文档对象中添加域
			//向文档对象中添加域
			document.addField("id", searchItem.getId());
			document.addField("item_title", searchItem.getTitle());
			document.addField("item_sell_point", searchItem.getSell_point());
			document.addField("item_price", searchItem.getPrice());
			document.addField("item_image", searchItem.getImage());
			document.addField("item_category_name", searchItem.getCategory_name());
			//把文档对象写入索引库
			httpSolrClient.add(document);
			//提交
			httpSolrClient.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		

	}

}
