import { Component, OnInit, ViewChild } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { ChartDataset, ChartOptions, ChartType } from 'chart.js';
import { BaseChartDirective } from 'ng2-charts';
import { DevicesService } from '../devices.service';

@Component({
  selector: 'app-monitoring-chart',
  templateUrl: './monitoring-chart.component.html',
  styleUrls: ['./monitoring-chart.component.css']
})
export class MonitoringChartComponent implements OnInit{

  @ViewChild(BaseChartDirective) chart: BaseChartDirective;
  constructor(private http: HttpClient, private deviceService: DevicesService) {}
  
  public barChartType: ChartType = 'bar';
  public barChartLabels: string[] = [];
  public barChartOptions: ChartOptions = {
    responsive: true,
  };
  private staticLabels: string[] = [];
  private formatDate(index: number): string {
    return this.staticLabels[index];
  }

  public barChartData: ChartDataset[] = [
    { data: [], 
      label: 'kWh',
      backgroundColor: 'rgba(128, 0, 128, 0.5)', 
      borderColor: 'rgba(0, 0, 0, 1)',
      borderWidth: 1 },
  ];

  fetchData(deviceId: number) {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
      })
    };
    this.http.get<any>(`http://localhost:8093/monitoring/getsum?device_id=${deviceId}`, httpOptions).subscribe(
      data => {
        this.barChartData[0].data = data;
        console.log(data);
        console.log(this.barChartData[0]);
        this.chart.update();
      },
      error => {
        console.log(error);
      }
    );
  }

  ngOnInit() {
    this.deviceService.selectedDeviceId$.subscribe((selectedDeviceId) => {
      console.log(selectedDeviceId);
      if (selectedDeviceId !== null) {
        this.fetchData(selectedDeviceId);
      }
    })
  } 

}
