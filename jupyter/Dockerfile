# File to build the Citi Jupyter docker container with files

FROM continuumio/anaconda3
LABEL version="1.0"
LABEL description="Jupyter Server for training with notebooks"
LABEL maintainer="Steve Shilling"
# Set env install variables
#RUN export SPARKMAGIC_CONF_DIR=/opt/dataflame/.sparkmagic BLPAPI_ROOT=/opt/dataflame/blpapi_cpp BLPAPI_INCDIR=/opt/dataflame/blpapi_cpp/include BLPAPI_LIBDIR=/opt/dataflame/blpapi_cpp/lib
RUN apt-get -y install build-essential
RUN /opt/conda/bin/conda install jupyter -y --quiet
# install sparkmagic dependencies ...
RUN pip install pbr mock msgpack argparse hdijupyterutils requests_kerberos kerberos autovizwidget plotly==2.7.0 'PANDAS>=0.22.0' nodejs npm sparkmagic
ADD dataflame-jupyter /opt/dataflame
RUN mkdir /opt/notebooks /root/.sparkmagic
ADD jNotebooks /opt/notebooks
RUN mkdir -p /opt/notebooks/static/base/images
RUN mv /opt/dataflame/favicon.ico /opt/notebooks/static/base/images
RUN mv /opt/dataflame/logo.png /opt/notebooks/static/base/images
RUN mv /opt/dataflame/.sparkmagic/config.json /root/.sparkmagic/config.json
# Enable ipywidgets
RUN jupyter nbextension enable --py --sys-prefix widgetsnbextension
# install sparkmagic from /opt/conda/lib/python3.7/site-packages
#RUN pip install -e  /opt/dataflame/sparkmagic
# Install the sparkmagic kernels ...
RUN jupyter-kernelspec install /opt/dataflame/sparkmagic/sparkmagic/kernels/sparkkernel --sys-prefix
RUN jupyter-kernelspec install /opt/dataflame/sparkmagic/sparkmagic/kernels/pysparkkernel --sys-prefix
RUN jupyter-kernelspec install /opt/dataflame/sparkmagic/sparkmagic/kernels/sparkrkernel --sys-prefix
RUN jupyter-kernelspec install /opt/dataflame/sparkmagic/sparkmagic/kernels/commlibpysparkkernel --sys-prefix
RUN jupyter-kernelspec install /opt/dataflame/sparkmagic/sparkmagic/kernels/mqascoobypysparkkernel --sys-prefix
# Enable sparkmagicserver extension ...
RUN jupyter-serverextension enable --py sparkmagic --sys-prefix
# configure sparkmagic ...
RUN python /opt/dataflame/scripts/setconfig.py
# install titus token server extension and nbextension ...
RUN pip install -e /opt/dataflame/titus_token
RUN jupyter-serverextension enable --py titus_token --sys-prefix
RUN jupyter-nbextension install --py titus_token --sys-prefix
RUN jupyter-nbextension enable --py titus_token --sys-prefix
# install PDBLP, Beautiful Soup v4
RUN pip install pdblp beautifulsoup4
COPY runjpt /opt/runjpt
RUN chmod +x /opt/runjpt
RUN mkdir /root/.jupyter
COPY jupyter_notebook_config.json /root/.jupyter/jupyter_notebook_config.json
# Trust all notebooks
RUN find /opt/notebooks -name '*.ipynb' -exec jupyter trust {} \;
VOLUME ["/opt/notebooks"]
EXPOSE 8888
#ENTRYPOINT ["/opt/runjpt"]
ENTRYPOINT ["/opt/conda/bin/jupyter","notebook","--notebook-dir=/opt/notebooks","--ip=0.0.0.0","--port=8888","--no-browser","--allow-root"]
