import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HotTableModule } from '@handsontable/angular';
import { AjaxLoadingModule } from '../../components/ajax-loading/ajax-loading.module';
import { AjaxPreloadModule } from '../../components/ajax-preload/ajax-preload.module';
import { LoadingRetryModule } from '../../components/loading-retry/loading-retry.module';
import { LoadingSpinnerModule } from '../../components/loading-spinner/loading-spinner.module';
import { PanelChevronModule } from '../../components/panel-chevron/panel-chevron.module';
import { StatusMessageModule } from '../../components/status-message/status-message.module';
import { InstructorCourseEnrollPageComponent } from './instructor-course-enroll-page.component';

const routes: Routes = [
  {
    path: '',
    component: InstructorCourseEnrollPageComponent,
  },
];

/**
 * Module for instructor course enroll page.
 */
@NgModule({
  declarations: [
    InstructorCourseEnrollPageComponent,
  ],
  exports: [
    InstructorCourseEnrollPageComponent,
  ],
  imports: [
    CommonModule,
    HotTableModule,
    StatusMessageModule,
    AjaxPreloadModule,
    RouterModule.forChild(routes),
    AjaxLoadingModule,
    LoadingRetryModule,
    LoadingSpinnerModule,
    PanelChevronModule,
  ],
})
export class InstructorCourseEnrollPageModule { }
