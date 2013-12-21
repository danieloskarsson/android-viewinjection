
Android View Injection Library
=====================

View "injection" Library for Android that uses reflection to invoke methods annotated with a given annotation. Methods parameters that are annotated with the @Inject(R.id.resId) annotation will get the corresponding view injected.

_Usage:_


	Invoke.methodsWithAnnotation(MethodAnnotation.class).inActivity(this)

_or:_


	Invoke.methodsWithAnnotation(MethodAnnotation.class).inFragment(this)
 
The method annotation can be any of the predefined ones, or a custom annotation. Any custom annotation must be of ElementType `METHOD` and have the RetentionPolicy `RUNTIME`.

Method invocations will always be executed on the main thread.