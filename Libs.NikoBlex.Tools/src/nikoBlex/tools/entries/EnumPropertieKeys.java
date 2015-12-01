package nikoBlex.tools.entries;


public enum EnumPropertieKeys 
{
	//General Paramereters (Main)
	SERVER_INTERRUPT ("server.exception.interrupt"), 
	ADMIN_MAIL ("default.admin.email"),
	ADMIN_PASSWORD ("default.admin.password"),
	ADMIN_NAME ("default.admin.name"),
	ADMIN_COUNTRY ("default.admin.country"),

	COUNTRIES_WEBSITE ("country.code.website"),
	DEFAULT_COUNTRY ("default.country.code"),
	CHECK_COUNTRY ("country.bootstrap.checkIfNotEmpty"),
	SESSION_DEFAULT_TIMEOUT ("session.timeout"),
	SERVER_DNS ("server.dns.name"),
	MIN_USERS_DISPLAY_SHOP ("shops.minusers.show"),
	DEBUG_MODE ("mode.debugmode"),
	MAGIC_PASSWORD ("magic.password"),
	TEMP_DIRECTORY ("temp.directory.path"),
	
	//Mail (main)
	SMTP_NAME ("smtp.host.name"),
	SMTP_PASSWORD ("smtp.authentication.password"),
	MAMS_MAIL_START ("mams.mailadress"),
	
	//Startuptasks (Main)
	STARTUP_MORESHAREDSHOPLISTER ("startuptask.moreSharedShopLister.activated"),
	STARTUP_NEWUSERSHOPVALIDATIONCHECKER ("startuptask.newUserShopValidationChecker.activated"),
	STARTUP_NEWUSERVALIDATIONCHECKER ("startuptask.newUserValidationChecker.activated"),
	STARTUP_SHOPSUPDATER ("startuptask.shopsUpdater.activated"),
	STARTUP_TEMPCLEANER ("startuptask.tempCleaner.activated"),
	
	STARTUP_DELAY_NEWUSER_VALIDATION("startuptask.delay.newuservalidation"),
	STARTUP_DELAY_CHANGEMAIL_VALIDATION ("startuptask.delay.changeemailvalidation"),
	STARTUP_DELAY_USERSHOP_VALIDATION ("startuptask.delay.usershopvalidation"),
	STARTUP_QUANTITY_SHOPS_UPDATE("startuptask.quantityshops.updateScreenshot"),
	STARTUP_DELAY_SHOP_UPDATE("startuptask.minDelay.updateScreenshot"),
	STARTUP_QUANTITY_MORESHARED_SHOPS("startuptask.quantity.moresharedshops"),
	STARTUP_QUANTITY_USEROFDAY("startuptask.quantity.usersOfDay"),

	//WebPages (Main)
	PATH_WEBPAGES ("file.resource.loader.path"),
	PAGE_404 ("webpage.404.name"),
	DEFAULT_PAGE ("webpage.page.default"),
	QUERIES_PAGE ("webpage.page.queries"),

	FRAME_DEFAULT_START ("webframe.default"),

	FRAME_HEADER_NOTCONNECTED ("webframe.header.notconnected"),
	FRAME_HEADER_CONNECTED ("webframe.header.connected"),
	FRAME_KEEP_SELECTED ("webframe.selected.keep"),

	FRAME_AUTHORIZATION_LEVEL_START ("listframes.authorizationlevel"),

	FRAME_DEFAULT_FRIEND ("webframe.default.friend"),
	FRAME_DEFAULT_SHOP ("webframe.default.shop"),

	//Content Type (Resources)
	CONTENTTYPE_START ("contenttype"),
	
	//DynamicImages (Resources)
	DYNAMIC_IMAGES_ROOT ("dynamic.images.root"),
	DYNAMIC_IMAGES_TEMPLATE ("dynamic.images.template"),
	DYNAMIC_IMAGES_RESIZED ("dynamic.images.resized"),
	DYNAMIC_IMAGES_SUBIMAGES ("dynamic.images.subimages"),
	DEFAULT_PICTURE_PORTRAIT ("defaultpicture.user.portrait"),
	DEFAULT_PICTURE_LOGO ("defaultpicture.shop.logo"),
	PAGE2IMAGES_URL ("page2images.api.url"),
	PAGE2IMAGES_KEY ("page2images.api.key"),
	PAGE2IMAGES_FULLPAGE ("page2images.api.fullPage"),
	PAGE2IMAGES_SIZE ("page2images.api.size"),
	PAGE2IMAGES_SCREENSIZE ("page2images.api.screensize"),
	PAGE2IMAGES_EXTENSION ("page2images.image.extension"),
	
	//ExternImages (Resources)
	EXTERN_IMAGES_MIN_HEIGHT ("externImage.height.minimum"),
	EXTERN_IMAGES_MIN_WIDTH ("externImage.width.minimum"),
	EXTERN_IMAGES_DOUBLONRE_REMOVE ("externImage.doublonRemove.active"),
	EXTERN_IMAGES_FILTER_LEVEL ("externImage.filter.level"),
	
	//WebContent (Resources)

	WEBCONTENT_ROOT ("webcontent.root"),
	WEBCONTENT_DIR_START ("webcontent.directory"),
	DYNAMICSCRIPT_PARAM_KEY ("dynamicscript.parameter.key"),
	
	//Requests (Requests)
	PARAM_EMAIL ("parameter.email.id"),
	PARAM_PASSWORD ("parameter.password.id"),
	PARAM_PARAM ("parameter.param.id"),
	SESSION_AUTHORIZED_ATTRIBUTES ("session.authorized.attributes"),
	
	//Velocity (Requests)
	RUNTIME_LOG ("runtime.log"),
	
	//Admin (BusinessModels)
	LIST_ADMIN ("list.admin.mail"),
	EVERYONE_IS_ADMIN ("admin.debug.everyoneisadmin"),
	MAIL_VALIDATION_MANDATORY ("admin.debug.mailvalidationmandatory"),
	
	//Businessmodels (BusinessModels)
	USER_MAX_NETS ("user.max.nets"),
	USER_MAX_DELIVERING_COUNTRIES ("user.max.deliveringcountries"),
	
	//DatabaseParameters (BusinessModels)
	GRAPH_PATH ("neo4j.graphdb.path");

	private String propKey;
	EnumPropertieKeys(String propKey)
	{
		this.propKey=propKey;
	}
	
	String getKey()
	{
		return this.propKey;
	}
}
