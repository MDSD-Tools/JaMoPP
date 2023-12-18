import { HttpClientTestingModule } from '@angular/common/http/testing';
import { inject, TestBed } from '@angular/core/testing';
import { TimezoneService } from '../../../services/timezone.service';
import { RecycleBinTableFormatDatePipe } from './recycle-bin-table-format-date.pipe';

describe('RecycleBinTableFormatDatePipe', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
  });

  it('create an instance', inject([TimezoneService], (timezoneService: TimezoneService) => {
    const pipe: RecycleBinTableFormatDatePipe = new RecycleBinTableFormatDatePipe(timezoneService);
    expect(pipe).toBeTruthy();
  }));
});
