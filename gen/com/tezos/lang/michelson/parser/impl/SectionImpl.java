// This is a generated file. Not intended for manual editing.
package com.tezos.lang.michelson.parser.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.tezos.lang.michelson.MichelsonTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.tezos.lang.michelson.parser.*;

public class SectionImpl extends ASTWrapperPsiElement implements Section {

  public SectionImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull Visitor visitor) {
    visitor.visitSection(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof Visitor) accept((Visitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public CodeSection getCodeSection() {
    return findChildByClass(CodeSection.class);
  }

  @Override
  @Nullable
  public ParameterSection getParameterSection() {
    return findChildByClass(ParameterSection.class);
  }

  @Override
  @Nullable
  public ReturnSection getReturnSection() {
    return findChildByClass(ReturnSection.class);
  }

  @Override
  @Nullable
  public StorageSection getStorageSection() {
    return findChildByClass(StorageSection.class);
  }

}
