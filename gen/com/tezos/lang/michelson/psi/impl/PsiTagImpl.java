// This is a generated file. Not intended for manual editing.
package com.tezos.lang.michelson.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.tezos.lang.michelson.MichelsonTypes.*;
import com.tezos.lang.michelson.psi.*;
import com.tezos.lang.michelson.lang.tag.TagMetadata;
import com.intellij.psi.tree.IElementType;

public class PsiTagImpl extends PsiDataImpl implements PsiTag {

  public PsiTagImpl(@NotNull IElementType type) {
    super(type);
  }

  @Override
  public <R> R accept(@NotNull PsiVisitor<R> visitor) {
    return visitor.visitTag(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiVisitor) accept((PsiVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<PsiData> getDataList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiData.class);
  }

  @Override
  @NotNull
  public List<PsiEmptyBlock> getEmptyBlockList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiEmptyBlock.class);
  }

  @Override
  @Nullable
  public PsiElement getTagToken() {
    return findPsiChildByType(TAG_TOKEN);
  }

  @Override
  @NotNull
  public String getTagName() {
    return MichelsonPsiUtil.getTagName(this);
  }

  @Override
  @Nullable
  public TagMetadata getTagMetadata() {
    return MichelsonPsiUtil.getTagMetadata(this);
  }

}
