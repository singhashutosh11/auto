package trial.keyStone.automation;


import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ExcelData
{

	private String						id = "Id";
	private String			            desc = "DESCRIPTION";
	private String						Skip = "SKIP";
	private String						suite = "SUITE";
	private boolean						skip;
	private String						description;

	private Map<String, String>			singleValueMap;

	public ExcelData(Map<String, String> map)
	{
		this.singleValueMap = map;
		if (singleValueMap.get(id) == null)
		{
			throw new RuntimeException("ID not defined");
		}
		this.id = singleValueMap.remove(id);
		this.description = singleValueMap.remove(desc);
		String skipText = singleValueMap.remove(Skip);
		this.skip = "Y".equalsIgnoreCase(skipText) || "Yes".equalsIgnoreCase(skipText)
				|| "True".equalsIgnoreCase(skipText);
	}

	@Override
	public boolean equals(Object other)
	{
		if (!(other instanceof ExcelData))
		{
			return false;
		}

		ExcelData otherData = (ExcelData) other;

		return id.equals(otherData.id);
	}

	@Override
	public int hashCode()
	{
		return id.hashCode();
	}

	@Override
	public String toString()
	{
		return description == null ? id : id + "(" + description + ")";
	}

	public Map<String, String> getData()
	{
		return singleValueMap;
	}

	public boolean skip()
	{
		return skip;
	}

	public String getId()
	{
		return id;
	}

	public String getSuites()
	{
		return singleValueMap.get(suite);
	}

}

