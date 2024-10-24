import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MonitoringChartComponent } from './monitoring-chart.component';

describe('MonitoringChartComponent', () => {
  let component: MonitoringChartComponent;
  let fixture: ComponentFixture<MonitoringChartComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MonitoringChartComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MonitoringChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
