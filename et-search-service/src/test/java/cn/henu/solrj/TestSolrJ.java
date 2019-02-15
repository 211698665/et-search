package cn.henu.solrj;

import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;



public class TestSolrJ {
	public static final String url="http://192.168.25.129:8983/solr/sywcore";
	@Test
	public void addDocument() throws Exception{
		//创建一个solrService对象，创建一个连接，参数solr服务的url
		HttpSolrClient client = new HttpSolrClient(url);
		//创建一个文档对象solrInputDocument
		SolrInputDocument doc = new SolrInputDocument();
		//向文档对象中添加域，文档中必须包含一个id域，所有的域的名称必须存在managed.xml中定义
		doc.addField("id", "doc01");
		doc.addField("item_title", "三国演义");
		doc.addField("item_price", 12.0);
		//把文档写入索引库
		client.add(doc);
		//提交
		client.commit();
	}
	
	@Test
	public void deleteDocument() throws Exception{
		HttpSolrClient solrClient = new HttpSolrClient(url);
		//删除文档
		solrClient.deleteById("doc01");//根据ID删除
		//solrClient.deleteByQuery("id:doc01");//根据查询删除，和上面的等价
		//提交
		solrClient.commit();
	}
	@Test
	public void QueryIndex() throws Exception{
		//创建一个HttpSolrClient对象
		HttpSolrClient solrClient = new HttpSolrClient(url);
		//创建一个SolrQuery对象
		SolrQuery solrQuery = new SolrQuery();
		//设置查询条件
		//solrQuery.setQuery("*:*");//查询所有
		solrQuery.set("q", "*:*");//查询所有，和上面的等价
		//执行查询QueryResponse
		QueryResponse queryResponse = solrClient.query(solrQuery);
		//获取分档列表,获取查询结果的总记录数
		SolrDocumentList solrDocumentList = queryResponse.getResults();
		System.out.println("总记录数"+solrDocumentList.getNumFound());
		//遍历文档列表，从文档中取域的内容
		for (SolrDocument solrDocument : solrDocumentList) {
			System.out.println(solrDocument.get("id"));
			System.out.println(solrDocument.get("item_title"));
			System.out.println(solrDocument.get("item_price"));
		}
	}
	@Test
	public void queryIndexFuZa()throws Exception{
		HttpSolrClient solrClient = new HttpSolrClient(url);
		//创建一个查询对象
		SolrQuery solrQuery = new SolrQuery();
		//设置查询条件
		solrQuery.setQuery("手机");//设置查询条件
		solrQuery.setStart(0);//设置从哪一个开始显示
		solrQuery.setRows(20);//设置显示的条数
		//设置默认搜索域为item_title
		solrQuery.set("df", "item_title");
		solrQuery.setHighlight(true);//开启高亮显示
		solrQuery.addHighlightField("item_title");//设置高亮显示的字段
		solrQuery.setHighlightSimplePre("<em>");//设置高亮的前缀
		solrQuery.setHighlightSimplePost("</em>");//设置高亮显示的后缀
		//执行查询
		QueryResponse queryResponse = solrClient.query(solrQuery);
		//获取分档列表,获取查询结果的总记录数
		SolrDocumentList solrDocumentList = queryResponse.getResults();
		System.out.println("总记录数"+solrDocumentList.getNumFound());
		//遍历文档列表，从文档中取域的内容
		for (SolrDocument solrDocument : solrDocumentList) {
			System.out.println(solrDocument.get("id"));
			//取高亮显示
			Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
			List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
			//判断取得的高亮节点是否为空
			String title="";
			if(list!=null&&list.size()>0) {
				title=list.get(0);
			}else {
				title=(String) solrDocument.get("item_title");
			}
			System.out.println(title);
			System.out.println(solrDocument.get("item_price"));
		}
	}
}
