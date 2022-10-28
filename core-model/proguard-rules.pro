 -keepattributes Annotation, InnerClasses
 -dontnote kotlinx.serialization.AnnotationsKt
 -dontnote kotlinx.serialization.SerializationKt

 # Keep Serializers

 -keep,includedescriptorclasses class com.ly.core_model.**$$serializer { *; }
 -keepclassmembers class com.ly.core_model.** {
     *** Companion;
 }
 -keepclasseswithmembers class com.ly.core_model.** {
     kotlinx.serialization.KSerializer serializer(...);
 }

 # When kotlinx.serialization.json.JsonObjectSerializer occurs

 -keepclassmembers class kotlinx.serialization.json.** {
     *** Companion;
 }
 -keepclasseswithmembers class kotlinx.serialization.json.** {
     kotlinx.serialization.KSerializer serializer(...);
 }