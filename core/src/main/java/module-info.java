open module com.github.kaiwinter.exifrename.core {
	requires org.slf4j;
	requires ch.qos.logback.core;
	requires metadata.extractor;
	
	exports com.github.kaiwinter.exifrename.uc;
	exports com.github.kaiwinter.exifrename.type;
}