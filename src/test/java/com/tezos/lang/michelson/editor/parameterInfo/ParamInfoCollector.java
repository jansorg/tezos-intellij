package com.tezos.lang.michelson.editor.parameterInfo;

import com.intellij.lang.parameterInfo.CreateParameterInfoContext;
import com.intellij.lang.parameterInfo.ParameterInfoHandler;
import com.intellij.lang.parameterInfo.UpdateParameterInfoContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.UserDataHolderEx;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.testFramework.utils.parameterInfo.MockParameterInfoUIContext;
import org.jetbrains.annotations.NotNull;

/**
 * We can't use a Kotlin class here because we support 145.x and later. 145.x misses a few implementations of interface methods of later builds.
 * In Kotlin we had to add an override flag and that won't compile with 145.x, although it would with the later versions.
 * In Java the annotation is not required.
 * @author jansorg
 */
public class ParamInfoCollector extends MockParameterInfoUIContext<PsiElement> implements CreateParameterInfoContext, UpdateParameterInfoContext {
    private final PsiFile file;
    private final int offset;
    private final Editor editor;
    private PsiElement highlightedElement;
    private Object[] items;
    private PsiElement owner;
    private Object highlightedParam;
    private int currentParam;
    private boolean hidden = true;

    public ParamInfoCollector(PsiFile file, int offset, Editor editor) {
        super(null);
        this.file = file;
        this.offset = offset;
        this.editor = editor;
    }

    @Override
    public Object[] getItemsToShow() {
        return items;
    }

    @Override
    public void setItemsToShow(Object[] items) {
        this.items = items;
    }

    @Override
    public void showHint(PsiElement element, int offset, ParameterInfoHandler handler) {
        this.hidden = false;
    }

    @Override
    public void removeHint() {
        this.hidden = true;
    }


    @Override
    public PsiElement getParameterOwner() {
        return this.owner;
    }

    @Override
    public void setParameterOwner(PsiElement o) {
        this.owner = o;
    }

    @Override
    public void setHighlightedParameter(Object parameter) {
        this.highlightedParam = parameter;
    }

    @Override
    public void setCurrentParameter(int index) {
        this.currentParam = index;
    }

    @Override
    public boolean isUIComponentEnabled(int index) {
        return false;
    }

    @Override
    public void setUIComponentEnabled(int index, boolean enabled) {

    }

    @Override
    public int getParameterListStart() {
        return offset;
    }

    @Override
    public Object[] getObjectsToView() {
        return new Object[0];
    }

    @Override
    public PsiElement getHighlightedElement() {
        return highlightedElement;
    }

    @Override
    public void setHighlightedElement(PsiElement elements) {
        this.highlightedElement = elements;
    }

    @Override
    public Project getProject() {
        return editor.getProject();
    }

    @Override
    public PsiFile getFile() {
        return file;
    }

    @Override
    public int getOffset() {
        return offset;
    }

    @NotNull
    @Override
    public Editor getEditor() {
        return editor;
    }

//    @Override
    public Object getHighlightedParameter() {
        return null;
    }

//    @Override
    public boolean isPreservedOnHintHidden() {
        return false;
    }

//    @Override
    public void setPreservedOnHintHidden(boolean b) {

    }

//    @Override
    public boolean isInnermostContext() {
        return false;
    }

//    @Override
    public UserDataHolderEx getCustomContext() {
        return null;
    }
}
