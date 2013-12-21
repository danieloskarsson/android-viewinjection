package com.danieloskarsson.viewinjection;

import android.app.Activity;
import android.app.Fragment;

import com.danieloskarsson.viewinjection.annotations.Inject;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Invoke methods that are annotated with the given method annotation
 *  and where the parameters are annotated with the @Inject annotation.
 *
 * The method annotation can be any of the predefined ones, or a custom annotation.
 * Any custom annotation must be of ElementType METHOD and have the RetentionPolicy RUNTIME.
 *
 * Usage: Invoke.methodsWithAnnotation(MethodAnnotation.class).inActivity(this)
 *    or: Invoke.methodsWithAnnotation(MethodAnnotation.class).inFragment(this)
 *
 * Method invocations will always be executed on the main thread.
 *
 * @author Daniel Oskarsson (daniel.oskarsson@gmail.com)
 */
public class Invoke {

	private final Class<? extends Annotation> annotation;

	private Invoke(Class<? extends Annotation> annotation) {
		this.annotation = annotation;
	}

	public static Invoke methodsWithAnnotation(final Class<? extends Annotation> annotation) {
		return new Invoke(annotation);
	}

	public void inActivity(final Activity activity) {
		inject(annotation, activity, activity);
	}

	public void inFragment(final Fragment fragment) {
		inject(annotation, fragment.getActivity(), fragment);
	}

	public void inFragment(final android.support.v4.app.Fragment fragment) {
		inject(annotation, fragment.getActivity(), fragment);
	}

	private void inject(final Class<? extends Annotation> annotation, final Activity activity, final Object object) {
		for (final Method method : object.getClass().getDeclaredMethods()) {
			if (method.isAnnotationPresent(annotation)) {
				final Annotation[][] parameterAnnotations = method.getParameterAnnotations();
				final Object[] parameterValues = new Object[parameterAnnotations.length];

				for (int i = 0; i < parameterAnnotations.length; i++) {
					for (Annotation parameterAnnotation : parameterAnnotations[i]) {
						if (parameterAnnotation instanceof Inject) {
							final Inject injectAnnotation = (Inject) parameterAnnotation;
							parameterValues[i] = activity.findViewById(injectAnnotation.value());
						}
					}
				}

				activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						try {
							method.invoke(object, parameterValues);
						} catch (InvocationTargetException e) {
							throw new IllegalStateException(e);
						} catch (IllegalAccessException e) {
							throw new IllegalStateException(e);
						}
					}
				});
			}
		}
	}

}
