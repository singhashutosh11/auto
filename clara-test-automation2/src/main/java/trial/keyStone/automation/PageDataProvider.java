package trial.keyStone.automation;

public interface PageDataProvider {
	<T> T getPageData(String dataSetName);

}
