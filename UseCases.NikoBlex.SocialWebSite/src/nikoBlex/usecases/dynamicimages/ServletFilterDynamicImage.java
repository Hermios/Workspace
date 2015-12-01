package nikoBlex.usecases.dynamicimages;

public class ServletFilterDynamicImage {
	static String getDynamicPath()
	{
		return DynamicImageManager.getDynamicImagesRoot();
	}

}
