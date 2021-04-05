
在项目的build.gradle中添加：

    	allprojects {
		repositories {
			maven { url 'https://jitpack.io' }
		}
	}
  
添加依赖关系：
 
     dependencies {
	        compile 'com.github.CorruptWood:Tablayout:1.1.3'
	 }
