import axios from 'axios'

const utils = {
  axiosMethod: (config) => {
    axios({
      method: config.method,
      url: config.url,
      headers: {
        'Content-Type': 'application/json',
        'Authorization': config.headers.Authorization ? config.headers.Authorization : 'zhang'
      },
      param: config.param ? config.params : null,
      data: config.data ? config.data : null
    })
      .then(config.callback).catch(config.catch ? config.catch : () => {})
  }

}

export  default  utils
