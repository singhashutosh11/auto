package trial.keyStone.automation;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlType;



public class XMLDeserializer {
	protected Map<DataSetId, Object>	dataSetMap	= new HashMap<DataSetId, Object>();

	@SuppressWarnings("restriction")
	public XMLDeserializer(String xml, Class<?> contextClass)
	{
		try
		{
			JAXBContext jc = JAXBContext.newInstance(contextClass);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			Object data = unmarshaller.unmarshal(new File(xml));
			createDataSetMap(data);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException("Could Not Initialize Page data provider", e);
		}
	}
	private void createDataSetMap(Object data) throws Exception
	{
		for (Field field : data.getClass().getDeclaredFields())
		{
			field.setAccessible(true);
			Object value = field.get(data);
			if (value instanceof List<?>)
			{
				List<?> list = (List<?>) value;
				for (Object pageData : list)
				{
					addToMap(pageData);
				}
			}
		}
	}

	private boolean hasIdField(Object pageData)
	{
		try
		{
			pageData.getClass().getDeclaredField("id");
			return true;
		}
		catch (NoSuchFieldException e)
		{
			return false;
		}
	}

	private void addToMap(Object pageData) throws Exception
	{
		if (hasIdField(pageData) && pageData.getClass().getAnnotation(XmlType.class) != null)
		{
			String type = pageData.getClass().getAnnotation(XmlType.class).name();
			Field idField = pageData.getClass().getDeclaredField("id");
			idField.setAccessible(true);
			String id = (String) idField.get(pageData);
			dataSetMap.put(new DataSetId(type, id), pageData);
		}
	}
	@SuppressWarnings("unchecked")
	public <T> T getDeserializedData(DataSetId id)
	{
		return (T) dataSetMap.get(id);
	}
	

}
