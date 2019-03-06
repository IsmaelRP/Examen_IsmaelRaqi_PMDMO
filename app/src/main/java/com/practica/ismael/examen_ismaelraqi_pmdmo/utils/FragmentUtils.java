package com.practica.ismael.examen_ismaelraqi_pmdmo.utils;


import androidx.annotation.AnimRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

@SuppressWarnings("unused")
public class FragmentUtils {

    private FragmentUtils() {
    }

    @SuppressWarnings("SameParameterValue")
    public static void replaceFragment(@NonNull FragmentManager fragmentManager,
                                       @IdRes int parentResId, @NonNull Fragment fragment, @NonNull String tag) {
        fragmentManager.beginTransaction().replace(parentResId, fragment, tag).commit();
    }

    @SuppressWarnings("SameParameterValue")
    public static void replaceFragmentAddToBackstack(@NonNull FragmentManager fragmentManager,
                                                     @IdRes int parentResId, @NonNull Fragment fragment, @NonNull String tag,
                                                     @NonNull String backstackTag, int transition) {
        fragmentManager.beginTransaction().replace(parentResId, fragment, tag).setTransition(
                transition).addToBackStack(backstackTag).commit();
    }

    public static void replaceFragmentAddToBackstackWithCustomAnimations(
            @NonNull FragmentManager fragmentManager, @IdRes int parentResId,
            @NonNull Fragment fragment, @NonNull String tag, @NonNull String backstackTag,
            @AnimRes int enterAnimation, @AnimRes int exitAnimation, @AnimRes int popEnterAnimation,
            @AnimRes int popExitAnimation) {
        fragmentManager.beginTransaction()
                .setCustomAnimations(enterAnimation, exitAnimation, popEnterAnimation,
                        popExitAnimation)
                .replace(parentResId, fragment, tag)
                .addToBackStack(backstackTag)
                .commit();
    }

}