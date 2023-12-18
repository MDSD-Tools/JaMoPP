import { Pipe, PipeTransform } from '@angular/core';
import { CommentOutput } from '../../../types/api-output';
import { CommentRowModel } from './comment-row/comment-row.component';

/**
 * Transforms comment to comment row model.
 */
@Pipe({
  name: 'commentToCommentRowModel',
})
export class CommentToCommentRowModelPipe implements PipeTransform {

  transform(comment: CommentOutput, timezone?: string): CommentRowModel {
    return {
      timezone,
      originalComment: comment,
      commentGiverName: comment.commentGiverName,
      lastEditorName: comment.lastEditorName,
      commentEditFormModel: {
        commentText: comment.commentText,
        isUsingCustomVisibilities: !comment.isVisibilityFollowingFeedbackQuestion,
        showCommentTo: comment.showCommentTo,
        showGiverNameTo: comment.showGiverNameTo,
      },
      isEditing: false,
    };
  }

}
