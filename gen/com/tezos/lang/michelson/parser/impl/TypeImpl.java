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

public class TypeImpl extends ASTWrapperPsiElement implements Type {

  public TypeImpl(@NotNull ASTNode node) {
    super(node);
  }

  public <R> R accept(@NotNull Visitor<R> visitor) {
    return visitor.visitType(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof Visitor) accept((Visitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public ComparableType getComparableType() {
    return findChildByClass(ComparableType.class);
  }

  @Override
  @Nullable
  public Type getType() {
    return findChildByClass(Type.class);
  }

  @Override
  @Nullable
  public TypeToplevel getTypeToplevel() {
    return findChildByClass(TypeToplevel.class);
  }

}
