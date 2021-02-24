package services;

import com.intellij.spellchecker.BundledDictionaryProvider;

public class BlenderDic implements BundledDictionaryProvider {
    @Override
    public String[] getBundledDictionaries() {
        return new String[]{"dic/blender.dic"};
    }
}
