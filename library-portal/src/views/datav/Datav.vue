<template>
  <div style="display: flex; justify-content: space-around;margin-top: 20px" >
    <div id="book" style="width: 500px; height: 500px">

    </div>
    <div id="reader" style="width: 500px; height: 500px">

    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'
export default {
  name: "Datav",
  data() {
    return {
      bookOption: {
        title: {
          text: '图书借阅数据统计',
          left: 'center'
        },
        tooltip: {
          trigger: 'item'
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {
            name: '图书借阅数据',
            type: 'pie',
            radius: '50%',
            data: [
              { value: 1048, name: 'Search Engine' },
              { value: 735, name: 'Direct' },
              { value: 580, name: 'Email' },
              { value: 484, name: 'Union Ads' },
              { value: 300, name: 'Video Ads' }
            ],
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      },
      readerOption: {
        title: {
          text: '读者阅读数据统计',
          left: 'center'
        },
          xAxis: {
            type: 'category',
            data: []
          },
          yAxis: {
            type: 'value',
            name: '书籍数量'
          },
          series: [
            {
              data: [],
              type: 'bar'
            }
          ]
      }
    }
  },
  mounted() {
    this.datavLendBooks()
    this.datavReaders()
  },
  methods: {
    datavLendBooks() {
      this.getRequest('lendInfo/datavLendBooks').then(res => {
        if (res) {
          let data = [];
          res.object.forEach(obj => {
            data.push(
                {
                  value: obj.count,
                  name: obj.book_name
                }
            )

          });
          let series = [
            {
              name: 'Access From',
              type: 'pie',
              radius: '50%',
              data: data,
              emphasis: {
                itemStyle: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
              }
            }
          ]
          this.bookOption.series = series
          const bookDom = document.getElementById('book');
          const bookChart = echarts.init(bookDom);
          bookChart.setOption(this.bookOption)
        }
      })
    },
    datavReaders() {
      this.getRequest('/lendInfo/datavReaders').then(res => {
        if (res) {
          const count = [];
          const data = [];
          res.object.forEach(obj => {
            count.push(obj.count)
            data.push(obj.reader_name)
          })
          const series = [
            {
              data:count,
              type: 'bar'
            }
          ]
          this.readerOption.series = series
          this.readerOption.xAxis.data = data
          const readerDom = document.getElementById('reader')
          const readerChart = echarts.init(readerDom)
          readerChart.setOption(this.readerOption)
        }
      })
    }
  }

}
</script>

<style scoped>

</style>