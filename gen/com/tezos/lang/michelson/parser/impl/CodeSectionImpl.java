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

public class CodeSectionImpl extends ASTWrapperPsiElement implements CodeSection {

  public CodeSectionImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull Visitor visitor) {
    visitor.visitCodeSection(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof Visitor) accept((Visitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public Instruction getInstruction() {
    return findNotNullChildByClass(Instruction.class);
  }

}
