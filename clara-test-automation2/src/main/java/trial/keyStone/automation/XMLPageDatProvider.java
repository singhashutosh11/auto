package trial.keyStone.automation;

import java.util.Map;


public class XMLPageDatProvider extends XMLDeserializer implements PageDataProvider{
	private Map<String, Map<String, String>>	scnarioData;
	private String								scenario	= AbstractSeleniumTest.DEFAULT;

	public XMLPageDatProvider(String xml, Map<String, Map<String, String>> scnarioData,
			Class<?> contextClass)
	{
		super(xml, contextClass);
		this.scnarioData = scnarioData;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getPageData(String tag)
	{
		Map<String, String> map = scnarioData.get(scenario);
		DataSetId id = new DataSetId(tag, map.get(tag));
		if ("NONE".equalsIgnoreCase(id.getDataSet().trim()))
			return null;
		T data = getDeserializedData(id);
		if (data != null)
			return data;
		else
			return (T) id.getDataSet().toString(); // just return value in the
	}


}
