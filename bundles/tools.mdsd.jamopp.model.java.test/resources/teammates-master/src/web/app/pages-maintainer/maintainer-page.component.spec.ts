import { HttpClientTestingModule } from '@angular/common/http/testing';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { LoaderBarModule } from '../components/loader-bar/loader-bar.module';
import { LoadingSpinnerModule } from '../components/loading-spinner/loading-spinner.module';
import { StatusMessageModule } from '../components/status-message/status-message.module';
import { TeammatesRouterModule } from '../components/teammates-router/teammates-router.module';
import { ToastModule } from '../components/toast/toast.module';
import { PageComponent } from '../page.component';
import { MaintainerPageComponent } from './maintainer-page.component';

describe('MaintainerPageComponent', () => {
  let component: MaintainerPageComponent;
  let fixture: ComponentFixture<MaintainerPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        PageComponent,
        MaintainerPageComponent,
      ],
      imports: [
        NgbModule,
        HttpClientTestingModule,
        LoaderBarModule,
        LoadingSpinnerModule,
        RouterTestingModule,
        StatusMessageModule,
        TeammatesRouterModule,
        ToastModule,
      ],
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MaintainerPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
