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
import com.tezos.lang.michelson.lang.instruction.InstructionMetadata;
import com.intellij.psi.tree.IElementType;

public class PsiGenericInstructionImpl extends PsiInstructionImpl implements PsiGenericInstruction {

  public PsiGenericInstructionImpl(@NotNull IElementType type) {
    super(type);
  }

  @Override
  public <R> R accept(@NotNull PsiVisitor<R> visitor) {
    return visitor.visitGenericInstruction(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiVisitor) accept((PsiVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiAnnotationList getAnnotationList() {
    return PsiTreeUtil.getChildOfType(this, PsiAnnotationList.class);
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
  public PsiTrailingAnnotationList getTrailingAnnotationList() {
    return PsiTreeUtil.getChildOfType(this, PsiTrailingAnnotationList.class);
  }

  @Override
  @NotNull
  public List<PsiType> getTypeList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiType.class);
  }

  @Override
  @NotNull
  public PsiElement getInstructionToken() {
    return findPsiChildByType(INSTRUCTION_TOKEN);
  }

  @Override
  @NotNull
  public List<PsiBlockInstruction> getInstructionBlocks() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiBlockInstruction.class);
  }

  @Override
  @Nullable
  public InstructionMetadata getInstructionMetadata() {
    return MichelsonPsiUtil.getInstructionMetadata(this);
  }

}
