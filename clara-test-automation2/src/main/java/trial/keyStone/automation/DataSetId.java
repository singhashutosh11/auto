package trial.keyStone.automation;


public class DataSetId {
	private final String	opTag;
	private final String	dataSet;

	public DataSetId(String opTag, String dataSet)
	{
		if ((opTag == null) || opTag.isEmpty())
		{
			throw new NullPointerException("tag is empty");
		}

		if ((dataSet == null) || "".equalsIgnoreCase(dataSet.trim()))
		{
			throw new NullPointerException("dataSet id is empty");
		}

		this.opTag = opTag;
		this.dataSet = dataSet;
	}

	public String getDataSet()
	{
		return dataSet;
	}

	public String getOpTag()
	{
		return opTag;
	}

	@Override
	public int hashCode()
	{
		return opTag.hashCode();
	}

	@Override
	public boolean equals(Object other)
	{
		if (!(other instanceof DataSetId))
		{
			return false;
		}

		DataSetId otherDataSetId = (DataSetId) other;

		return opTag.equals(otherDataSetId.opTag) && dataSet.equals(otherDataSetId.dataSet);
	}

}
