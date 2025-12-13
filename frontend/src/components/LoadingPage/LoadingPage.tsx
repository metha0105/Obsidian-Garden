import "./LoadingPage.css";
import loadingGif from "../../assets/mew-loading.gif";

type LoadingPageProps = {
    message?: string;
    showRetry?: boolean;
}

export default function LoadingPage({
    message = "Waking up server... Render (free-tier) can take a bit of time to spin up after inactivity.",
    showRetry = false,
}: LoadingPageProps) {
    return (
        <div className="loading-page">
            <img
                src={loadingGif}
                alt="Waking up server"
                className="loading-gif"
            />
            <p className="loading-text">{message}</p>

            {showRetry && (
                <button className="loading-retry" onClick={() => window.location.reload()}>
                    RETRY
                </button>
            )}
        </div>
    );
}