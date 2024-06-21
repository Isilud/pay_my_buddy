import { Link, useLocation } from "react-router-dom";
import "./Breadcrumb.scss";

export default function Breadcrumb(): JSX.Element {
  return <div className="breadcrumbs">{useBreadcrumbs()}</div>;
}

function useBreadcrumbs(): JSX.Element[] {
  const location = useLocation();
  const pathArray =
    location.pathname === "/" ? [""] : location.pathname.split("/");
  let breadcrumbs: JSX.Element[] = [];
  while (pathArray.length > 0) {
    const name = pathArray.pop() ?? "";
    breadcrumbs.push(
      <Link className="breadcrumbs__link" to={"/" + pathArray.join("/") + name}>
        {getLabel(name)}
      </Link>
    );
    if (pathArray.length > 0) breadcrumbs.push(<span>/</span>);
  }
  return breadcrumbs.reverse();
}

function getLabel(location: string): string {
  switch (location) {
    case "":
      return "Accueil";
    case "Transfer":
      return "Transfert";
    case "Profile":
      return "Profil";
    case "Contact":
      return "Contact";
    default:
      return "Undefined";
  }
}
