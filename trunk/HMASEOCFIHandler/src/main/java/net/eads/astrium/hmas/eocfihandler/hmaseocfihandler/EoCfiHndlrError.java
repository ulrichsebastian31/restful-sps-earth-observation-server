package net.eads.astrium.hmas.eocfihandler.hmaseocfihandler;

public class EoCfiHndlrError extends Exception {

	private static final long serialVersionUID = 1L;
	private boolean m_bCFIError;
        
        public EoCfiHndlrError( String sErrorMessage )
        {
            super( sErrorMessage );
            m_bCFIError = false;
        }
        	
	public void setCfiErrorFlag()
	{
            m_bCFIError = true;
	}
	
	public void setNonCfiErrorFlag()
	{
            m_bCFIError = false;
	}
	
	public boolean getCfiErrorFlag()
	{
            return m_bCFIError;
	}
}
